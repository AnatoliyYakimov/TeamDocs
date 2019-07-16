package com.yakimov.teamdocs.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

import com.yakimov.teamdocs.controllers.DocumentRestController;
import com.yakimov.teamdocs.entities.Document;
import com.yakimov.teamdocs.entities.DocumentModel;
import com.yakimov.teamdocs.exceptions.DocumentNotFoundException;

@Service
public class DocumentResourceAssembler extends RepresentationModelAssemblerSupport<Document, DocumentModel> {

	public DocumentResourceAssembler() {
		super(Document.class, DocumentModel.class);
	}

	@Override
	public DocumentModel toModel(Document document) {
		DocumentModel resource = instantiateModel(document);
		Link history = historyLink(document.getHash());
		Link last = lastVersionLink(document.getHash());
		Link self = selfLink(document.getId());
		resource.add(self, last, history);

		return resource;
	}

	@Override
	protected DocumentModel instantiateModel(Document entity) {
		return new DocumentModel(entity);
	}

	public PagedModel<DocumentModel> toPagedModel(Page<Document> page) {
		PageMetadata metadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(),
				page.getTotalPages());
		PagedModel<DocumentModel> model = new PagedModel<>(toCollectionModel(page.getContent()).getContent(), metadata);
		return model;
	}
	
	

	public List<DocumentModel> toListModel(Iterable<? extends Document> entities) {
		var docs = new ArrayList<DocumentModel>();
		entities.forEach((e) -> {
			docs.add(toModel(e));
		});
		return docs;
	}

	private Link historyLink(String hash) {
		return linkTo(methodOn(DocumentRestController.class).getHistoryOfDocumentByHash(hash, Pageable.unpaged()))
				.withRel("history");
	}

	private Link lastVersionLink(String hash) {
		try {
			return linkTo(methodOn(DocumentRestController.class).getLastVarsionOfDocumentByHash(hash))
					.withRel("last_version");
		} catch (DocumentNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private Link selfLink(Long id) {
		try {
			return linkTo(methodOn(DocumentRestController.class).getDocumentById(id)).withRel("self");
		} catch (DocumentNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
