import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISubmissions } from 'app/shared/model/submissions.model';
import { SubmissionsService } from './submissions.service';

@Component({
    selector: 'jhi-submissions-delete-dialog',
    templateUrl: './submissions-delete-dialog.component.html'
})
export class SubmissionsDeleteDialogComponent {
    submissions: ISubmissions;

    constructor(
        private submissionsService: SubmissionsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.submissionsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'submissionsListModification',
                content: 'Deleted an submissions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-submissions-delete-popup',
    template: ''
})
export class SubmissionsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ submissions }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SubmissionsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.submissions = submissions;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
