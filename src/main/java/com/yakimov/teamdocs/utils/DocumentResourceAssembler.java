package com.yakimov.teamdocs.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
		try {
			Link history = linkTo(methodOn(DocumentRestController.class)
					.getHistoryOfDocumentByHash(document.getHash(), Pageable.unpaged())).withRel("history");
			Link self = linkTo(methodOn(DocumentRestController.class).getDocumentById(document.getId())).withRel("self");
			Link last = linkTo(methodOn(DocumentRestController.class).getLastVarsionOfDocumentByHash(document.getHash())).withRel("last_version");
			resource.add(self, last, history);
		} catch (DocumentNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resource;
	}

	@Override
	protected DocumentModel instantiateModel(Document entity) {
		return new DocumentModel(entity);
	}

	public PagedModel<DocumentModel> toPagedModel(Page<Document> page){
		PageMetadata metadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
		PagedModel<DocumentModel> model = new PagedModel<>(toCollectionModel(page.getContent()).getContent(), metadata);
		return model;
	}
}
